package com.learnspigot.bot.voicechat

import io.github.cdimascio.dotenv.dotenv
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class VCListener : ListenerAdapter() {

    override fun onGuildVoiceUpdate(event: GuildVoiceUpdateEvent){
        val guild = event.guild
        val voiceChannel = guild.getVoiceChannelById(dotenv().get("VOICE_CHANNEL_ID"))
        val joinedChannel: AudioChannelUnion? = event.channelJoined
        val leftChannel = event.channelLeft

        val oldChannel = event.oldValue

        if (joinedChannel != null && joinedChannel == voiceChannel){

            oldChannel?.delete()?.queue()

            val newChannel = guild.createVoiceChannel("${event.member.effectiveName}'s channel", joinedChannel.parentCategory!!).complete()
            guild.moveVoiceMember(event.member, newChannel).queue()
            return
        }

        if (leftChannel != null){
            val channelMembers = leftChannel.members
            if (channelMembers.isEmpty() && leftChannel != voiceChannel){
                leftChannel.delete().queue()
            }
        }
    }
}